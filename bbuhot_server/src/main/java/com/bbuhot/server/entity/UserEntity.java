package com.bbuhot.server.entity;

import com.google.errorprone.annotations.RestrictedApi;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.jetbrains.annotations.TestOnly;

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

  public UserEntity() {}

  public int getUid() {
    return uid;
  }

  @RestrictedApi(explanation = "Test only", link = "", whitelistAnnotations = {TestOnly.class})
  public void setUid(int uid) {
    this.uid = uid;
  }

  public String getUsername() {
    return username;
  }

  @RestrictedApi(explanation = "Test only", link = "", whitelistAnnotations = {TestOnly.class})
  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  @RestrictedApi(explanation = "Test only", link = "", whitelistAnnotations = {TestOnly.class})
  public void setPassword(String password) {
    this.password = password;
  }

  public int getGroupId() {
    return groupId;
  }

  @RestrictedApi(explanation = "Test only", link = "", whitelistAnnotations = {TestOnly.class})
  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }
}
