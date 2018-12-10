package com.bbuhot.server.domain;

import com.bbuhot.server.app.Flags;
import com.bbuhot.server.entity.User;
import com.bbuhot.server.persistence.UserQueries;
import com.bbuhot.server.service.AuthDto;
import com.bbuhot.server.service.AuthRequest;
import com.bbuhot.server.service.ErrorCode;
import com.bbuhot.server.service.UserDto;
import java.util.Optional;
import javax.inject.Inject;

public class Authority {

  private final UserQueries userQueries;

  @Inject
  Authority(UserQueries userQueries) {
    this.userQueries = userQueries;
  }

  public AuthDto auth(AuthRequest authRequest) {
    Optional<User> optionalUser = userQueries.queryUserById(authRequest.getUid());
    if (optionalUser.isEmpty()) {
      return AuthDto.newBuilder().setErrorCode(ErrorCode.NO_SUCH_USER).build();
    }

    User user = optionalUser.get();
    if (!AuthorityUtil.isValid(
        Flags.getInstance().getDiscuzConfig().getAuthkey(),
        authRequest.getSaltKey(),
        authRequest.getAuth(),
        user.getUid(),
        user.getPassword())) {
      return AuthDto.newBuilder().setErrorCode(ErrorCode.KEY_NOT_MATCHING).build();
    }

    return AuthDto.newBuilder()
        .setErrorCode(ErrorCode.NO_ERROR)
        .setUser(UserDto.newBuilder().setUid(user.getUid()).setName(user.getUsername()))
        .build();
  }
}
