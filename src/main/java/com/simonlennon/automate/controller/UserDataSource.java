package com.simonlennon.automate.controller;

/**
 * Created by simon.lennon on 09/12/2014.
 */
public interface UserDataSource {

    void addUserDataListiner(UserDataListiner userDataListiner);

    void removeUserDataListiner(UserDataListiner userDataListiner);

}
