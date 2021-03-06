package com.bbuhot.server.domain;

import com.bbuhot.server.config.Flags;
import com.bbuhot.server.domain.AuthorityUtil.AuthResult;
import com.bbuhot.server.entity.UserEntity;
import com.bbuhot.server.persistence.UserQueries;
import com.bbuhot.server.service.AuthReply;
import com.bbuhot.server.service.AuthReply.AuthErrorCode;
import com.bbuhot.server.service.AuthRequest;

import javax.inject.Inject;

public class Authority {

  private final UserQueries userQueries;

  @Inject
  Authority(UserQueries userQueries) {
    this.userQueries = userQueries;
  }

  public AuthReply auth(AuthRequest authRequest, boolean checkIsAdmin) {
    AuthResult result = AuthorityUtil.getAuthResult(
        Flags.getInstance().getDiscuzConfig().getAuthKey(),
        authRequest.getSaltKey(),
        authRequest.getAuth());

    UserEntity userEntity =
        userQueries
            .queryUserById(result.getUid())
            .orElseGet(
                () -> {
                  throw new IllegalStateException("No user with such uid: " + result.getUid());
                });

    if (!userEntity.getPassword().equals(result.getPassword())) {
      return AuthReply.newBuilder().setErrorCode(AuthReply.AuthErrorCode.KEY_NOT_MATCHING).build();
    }

    if (checkIsAdmin && !isAdminGroup(userEntity)) {
      return AuthReply.newBuilder().setErrorCode(AuthErrorCode.PERMISSION_DENY).build();
    }

    return AuthReply.newBuilder()
        .setErrorCode(AuthReply.AuthErrorCode.NO_ERROR)
        .setUser(
            AuthReply.User.newBuilder()
                .setUid(userEntity.getUid())
                .setName(userEntity.getUsername()))
        .build();
  }

    /**
     * Very simple admin group check. We might need to support more in future.
     */
    private boolean isAdminGroup(UserEntity userEntity) {
    return Flags.getAdminGroups().contains(userEntity.getGroupId());
  }
}
