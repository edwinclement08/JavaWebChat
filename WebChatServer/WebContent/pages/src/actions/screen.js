import {postData} from "./_util";

export const hostname = "http://192.168.0.192:8080";

export const SHOW_SCREEN = "SHOW_SCREEN";
export const SCREEN_NOT_FOUND = "SCREEN_NOT_FOUND";

export const LOAD_USER_FRIENDS = "LOAD_USER_FRIENDS";
export const LOAD_USER_FRIENDS_SUCCESS = "LOAD_USER_FRIENDS_SUCCESS";

export const LOAD_USER_CHAT_MESSAGES = "LOAD_USER_CHAT_MESSAGES";
export const LOAD_USER_CHAT_MESSAGES_SUCCESS = "LOAD_USER_CHAT_MESSAGES_SUCCESS";

export const LOAD_USER_FRIENDS_ALL_MESSAGE = "LOAD_USER_FRIENDS_ALL_MESSAGE";
export const LOAD_USER_FRIENDS_ALL_MESSAGE_FINISH = "LOAD_USER_FRIENDS_ALL_MESSAGE_FINISH";
export const LOAD_FAILURE = "LOAD_FAILURE";
export const LOAD_ERROR = "LOAD_ERROR";


export function showScreen(name = "home") {
  let listOfPages = ["home", "allUsers", "help"];
  if (listOfPages.includes(name)) {
    return {
      type: SHOW_SCREEN,
      name: name,
    }
  } else {
    return {
      type: SCREEN_NOT_FOUND,
    }
  }
}

export function getAllMessagesForUser(username, token) {
  return dispatch => {
    dispatch({type: LOAD_USER_FRIENDS_ALL_MESSAGE});
    return postData(hostname + "/rest/message/getAllMessages/",
      {
        "username": username,
        "token": token
      }
    ).then((result) => {
      if (result.status === "true") {
        return dispatch({type: LOAD_USER_FRIENDS_ALL_MESSAGE_FINISH, content: result.contents});
      } else {
        return dispatch({type: LOAD_FAILURE, requestName: "getAllMessagesForUser", message: result.message});
      }
    }, (err) => {
      console.log(err);
      return dispatch({type: LOAD_ERROR, requestName: "getAllMessagesForUser"})
    })
  }
}

export function getAllFriendsForUser() {
  return (dispatch, getState) => {
    dispatch({type: LOAD_USER_FRIENDS});
    let username = getState().login.userData.user;
    let token = getState().login.userData.token;
    console.log("data from the store:" + username + "," + token);   //TODO remove this
    return postData(hostname + "/rest/message/getUserFriends/",
      {
        "username": username,
        "token": token
      }
    ).then((result) => {
      if (result.status === true) {
        console.log(result.friends);
        return dispatch({type: LOAD_USER_FRIENDS_SUCCESS, friends: result.friends});

      } else {
        return dispatch({type: LOAD_FAILURE, requestName: "getAllFriendsForUser", message: result.message});
      }
    }, (err) => {
      console.log(err);
      return dispatch({type: LOAD_ERROR, requestName: "getAllFriendsForUser"})
    })
  }
}

export const LOAD_USER_FRIENDS_MESSAGE_PEEK = "LOAD_USER_FRIENDS_MESSAGE_PEEK";
export const LOAD_USER_FRIENDS_MESSAGE_PEEK_SUCCESS = "LOAD_USER_FRIENDS_MESSAGE_PEEK_SUCCESS";

export function peekMessagesForUserWithFriend(friend) {
  return (dispatch, getState) => {
    dispatch({type: LOAD_USER_FRIENDS_MESSAGE_PEEK});
    let username = getState().login.userData.user;
    let token = getState().login.userData.token;
    return postData(hostname + "/rest/message/peekMessages/",
      {
        "username": username,
        "token": token
      }
    ).then((result) => {
      if (result.status === true) {
        return dispatch({type: LOAD_USER_FRIENDS_MESSAGE_PEEK_SUCCESS, friends: result.friends});
      } else {
        return dispatch({type: LOAD_FAILURE, requestName: "peekMessagesForUserWithFriend", message: result.message});
      }
    }, (err) => {
      console.log(err);
      return dispatch({type: LOAD_ERROR, requestName: "peekMessagesForUserWithFriend"})
    })
  }
}



