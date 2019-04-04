import {postData, getData, hostname} from "./_util";

export const SHOW_SCREEN = "SHOW_SCREEN";
export const SCREEN_NOT_FOUND = "SCREEN_NOT_FOUND";

export const LOAD_USER_FRIENDS = "LOAD_USER_FRIENDS";
export const LOAD_USER_FRIENDS_SUCCESS = "LOAD_USER_FRIENDS_SUCCESS";

export const LOAD_USER_FRIEND_ALL_MESSAGE_FINISH = "LOAD_USER_FRIEND_ALL_MESSAGE_FINISH";
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

export function getAllMessagesForUser(other_user) {
  return (dispatch, getState) => {
    let {user, token} = getState().login.userData;

    return postData(hostname + "/rest/message/getAllMessages/",
      {
        "username": user,
        "token": token,
        "friend": other_user,
      }
    ).then((result) => {
        if (result.status === true) {
          return dispatch({type: LOAD_USER_FRIEND_ALL_MESSAGE_FINISH, friend: other_user, contents: result.contents});
        } else {
          console.log(result);
          return dispatch({type: LOAD_FAILURE, requestName: "getAllMessagesForUser", message: result.message});
        }
      }, (err) => {
        console.log(err);
        return dispatch({type: LOAD_ERROR, requestName: "getAllMessagesForUser"})
      }
    )
  }
}

export function getAllFriendsForUser() {
  return (dispatch, getState) => {
    dispatch({type: LOAD_USER_FRIENDS});
    let username = getState().login.userData.user;
    let token = getState().login.userData.token;
    return postData(hostname + "/rest/message/getUserFriends/",
      {
        "username": username,
        "token": token
      }
    ).then((result) => {
      if (result.status === true) {
        result.friends.forEach((friend) => {
          dispatch(getAllMessagesForUser(friend))
        });
        // if (result.friends == []) {
        //   return dispatch({type: LOAD_USER_FRIENDS_SUCCESS, friends: []});
        // }

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

export const SEND_MESSAGE = "SEND_MESSAGE";
export const SEND_MESSAGE_SUCCESS = "SEND_MESSAGE_SUCCESS";
export const SEND_MESSAGE_FAILURE = "SEND_MESSAGE_FAILURE";
export const SEND_MESSAGE_ERROR = "SEND_MESSAGE_ERROR";

export const CHAT_SENT = "CHAT_SENT";     // needed to refresh list
export const chatSentEvent = (sender, receiver, dispatch, state) => {
  let friendList = state.screen.details.home.friends;
  if (!friendList.includes(receiver)) {
    dispatch(getAllFriendsForUser());
  }
};

export function sendMessage(receiver, content) {
  return (dispatch, getState) => {
    dispatch({type: SEND_MESSAGE, content: content});
    let {user, token} = getState().login.userData;
    return postData(hostname + "/rest/message/sendMessage/",
      {
        "username": user,
        "token": token,
        "receiver": receiver,
        "content": content,
      }
    ).then((result) => {
        if (result.status === true) {
          setTimeout(() => dispatch(getAllMessagesForUser(receiver)), 70);
          chatSentEvent(user, receiver, dispatch, getState());
          return dispatch({
            type: SEND_MESSAGE_SUCCESS, receiver: receiver, contents:
              {
                "username": user,
                "receiver": receiver,
                "message": content,
                "timeSent": (new Date()).getTime() / 1000
              }
          });
        } else {
          console.log(result);
          return dispatch({type: SEND_MESSAGE_FAILURE, requestName: "sendMessage", message: result.message});
        }
      }, (err) => {
        console.log(err);
        return dispatch({type: SEND_MESSAGE_ERROR, requestName: "sendMessage", message: "Network/API issues"})
      }
    )
  }
}

export const LOAD_ALL_USERS = "LOAD_ALL_USERS";
export const LOAD_ALL_USERS_SUCCESS = "LOAD_ALL_USERS_SUCCESS";
export const LOAD_ALL_USERS_ERROR = "LOAD_ALL_USERS_ERROR";

export function getAllUsers() {
  return (dispatch) => {
    dispatch({type: LOAD_ALL_USERS});
    return getData(hostname + "/rest/user/").then((result) => {
        return dispatch({type: LOAD_ALL_USERS_SUCCESS, users: result});
      }, (err) => {
        console.log(err);
        return dispatch({type: LOAD_ALL_USERS_ERROR, requestName: "getAllUsers"})
      }
    )
  }
}

export const RELOAD_ALL_DATA = "RELOAD_ALL_DATA";

export const TIMER_START = "TIMER_START";
export const TIMER_STOP = "TIMER_STOP";

export var globalTimer = null;

export const globalTimerStart = () => (dispatch, getState) => {
    clearInterval(globalTimer);
    globalTimer = setInterval(() => {
      if (getState().login.userData.loggedIn) {
        dispatch({type: RELOAD_ALL_DATA});
        dispatch(getAllFriendsForUser());
      }
    }, 5000);

    dispatch({type: TIMER_START});
  }
;


export const globalTimerStop = () => {
  clearInterval(globalTimer);
  return {type: TIMER_STOP};
};


// export const LOAD_USER_FRIENDS_MESSAGE_PEEK = "LOAD_USER_FRIENDS_MESSAGE_PEEK";
// export const LOAD_USER_FRIENDS_MESSAGE_PEEK_SUCCESS = "LOAD_USER_FRIENDS_MESSAGE_PEEK_SUCCESS";
//
// export function peekMessagesForUserWithFriend(friend) {
//   return (dispatch, getState) => {
//     dispatch({type: LOAD_USER_FRIENDS_MESSAGE_PEEK});
//     let username = getState().login.userData.user;
//     let token = getState().login.userData.token;
//     return postData(hostname + "/rest/message/peekMessages/",
//       {
//         "username": username,
//         "token": token
//       }
//     ).then((result) => {
//       if (result.status === true) {
//         return dispatch({type: LOAD_USER_FRIENDS_MESSAGE_PEEK_SUCCESS, friends: result.friends});
//       } else {
//         return dispatch({type: LOAD_FAILURE, requestName: "peekMessagesForUserWithFriend", message: result.message});
//       }
//     }, (err) => {
//       console.log(err);
//       return dispatch({type: LOAD_ERROR, requestName: "peekMessagesForUserWithFriend"})
//     })
//   }
// }
