package com.bbuhot.server.service;

import com.bbuhot.server.entity.ExtcreditsEntity;
import com.bbuhot.server.entity.GameEntity;
import com.bbuhot.server.entity.GameEntity.BettingOptionEntity;
import com.bbuhot.server.entity.UserEntity;
import com.bbuhot.server.persistence.ExtcreditsQueries;
import com.bbuhot.server.persistence.GameQueries;
import com.bbuhot.server.persistence.GameQueries.GameEntityStatus;
import com.bbuhot.server.persistence.UserQueries;
import com.bbuhot.server.util.TestMessageUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnit4.class)
public class BetUpdatingServiceTest {

    @Inject
    BetUpdatingService betUpdatingService;

    @Inject
    ExtcreditsQueries extcreditsQueries;
    @Inject
    GameQueries gameQueries;
    @Inject
    UserQueries userQueries;
    @Inject
    EntityManagerFactory entityManagerFactory;

    private TestMessageUtil testMessageUtil;

    @Before
    public void setUp() {
        testMessageUtil = new TestMessageUtil("com/bbuhot/server/service/res/");
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        TestServiceComponent.getInstance().inject(this);

        // create user
        UserEntity user = new UserEntity();
        user.setUid(1);
        user.setUsername("admin");
        user.setGroupId(1);
        user.setPassword("b2b29e4862ffe7a5d1388d1722a9aae5");
        userQueries.save(user);

        // create credits
        ExtcreditsEntity credits = new ExtcreditsEntity();
        credits.setUid(1);
        credits.setExtcredits2(500);
        extcreditsQueries.save(credits);

        // create game
        GameEntity game = new GameEntity();
        game.setId(1);
        game.setName("Ti8 Grand final LGD vs OG");
        game.setDescription("Ti8 Grand final LGD vs OG");
        game.setNormalUserVisible(true);
        game.setStatus(GameEntityStatus.PUBLISHED.value);
        game.setBetOptionLimit(2);
        game.setBetAmountLowest(100);
        game.setBetAmountHighest(400);
        game.setEndTimeMs(new Timestamp(System.currentTimeMillis() + 86400000));
        game.setWinningBetOption(-2);
        BettingOptionEntity option1 = new BettingOptionEntity();
        option1.setName("LGD win");
        option1.setOdds(2000000);
        game.getBettingOptionEntities().add(option1);
        BettingOptionEntity option2 = new BettingOptionEntity();
        option2.setName("OG win");
        option2.setOdds(8000000);
        game.getBettingOptionEntities().add(option2);
        BettingOptionEntity option3 = new BettingOptionEntity();
        option3.setName("Draw");
        option3.setOdds(20000000);
        game.getBettingOptionEntities().add(option3);
        gameQueries.save(game);
    }

    @After
    public void tearDown() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Test
    public void testBettingWorkflow() {
        // bet on game
        BetRequest betRequest =
                testMessageUtil.getResourcesAsMessage(
                        BetRequest.getDefaultInstance(), "create_bets_request.json");
        BetReply betReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        BetReply.getDefaultInstance(), "create_bets_reply.json");

        BetReply betReply = betUpdatingService.callProtobufServiceImpl(betRequest);
        assertEquals(betReplyExpected, betReply);

        // change bets
        betRequest =
                testMessageUtil.getResourcesAsMessage(
                        BetRequest.getDefaultInstance(), "change_bets_request.json");
        betReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        BetReply.getDefaultInstance(), "change_bets_reply.json");

        betReply = betUpdatingService.callProtobufServiceImpl(betRequest);
        assertEquals(betReplyExpected, betReply);

        // withdraw bets
        betRequest =
                testMessageUtil.getResourcesAsMessage(
                        BetRequest.getDefaultInstance(), "withdraw_bets_request.json");
        betReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        BetReply.getDefaultInstance(), "withdraw_bets_reply.json");

        betReply = betUpdatingService.callProtobufServiceImpl(betRequest);
        assertEquals(betReplyExpected, betReply);
    }

    @Test
    public void testBettingBelowLowest() {
        BetRequest betRequest =
                testMessageUtil.getResourcesAsMessage(
                        BetRequest.getDefaultInstance(), "bet_below_lowest_request.json");
        BetReply betReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        BetReply.getDefaultInstance(), "bet_below_lowest_reply.json");

        BetReply betReply = betUpdatingService.callProtobufServiceImpl(betRequest);
        assertEquals(betReplyExpected, betReply);
    }

    @Test
    public void testBettingAboveHighest() {
        BetRequest betRequest =
                testMessageUtil.getResourcesAsMessage(
                        BetRequest.getDefaultInstance(), "bet_above_highest_request.json");
        BetReply betReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        BetReply.getDefaultInstance(), "bet_above_highest_reply.json");

        BetReply betReply = betUpdatingService.callProtobufServiceImpl(betRequest);
        assertEquals(betReplyExpected, betReply);
    }

    @Test
    public void testBettingOnTooManyOptions() {
        BetRequest betRequest =
                testMessageUtil.getResourcesAsMessage(
                        BetRequest.getDefaultInstance(), "bet_on_too_many_options_request.json");
        BetReply betReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        BetReply.getDefaultInstance(), "bet_on_too_many_options_reply.json");

        BetReply betReply = betUpdatingService.callProtobufServiceImpl(betRequest);
        assertEquals(betReplyExpected, betReply);
    }

    @Test
    public void testBettingOverCapacity() {
        BetRequest betRequest =
                testMessageUtil.getResourcesAsMessage(
                        BetRequest.getDefaultInstance(), "bet_over_capacity_request.json");
        BetReply betReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        BetReply.getDefaultInstance(), "bet_over_capacity_reply.json");

        BetReply betReply = betUpdatingService.callProtobufServiceImpl(betRequest);
        assertEquals(betReplyExpected, betReply);
    }
}
