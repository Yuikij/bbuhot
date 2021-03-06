package com.bbuhot.server.entity;

import com.bbuhot.errorprone.TestOnly;

import javax.persistence.*;

// TODO(yh_victor): we should not need to support custom prefix.
@Table(name = "pre_common_member")
@Entity()
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, updatable = false, nullable = false)
  private int uid;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private int groupId;

    public UserEntity() {
    }

  public int getUid() {
    return uid;
  }

  @TestOnly
  public void setUid(int uid) {
    this.uid = uid;
  }

  public String getUsername() {
    return username;
  }

  @TestOnly
  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  @TestOnly
  public void setPassword(String password) {
    this.password = password;
  }

  public int getGroupId() {
    return groupId;
  }

  @TestOnly
  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }
}
