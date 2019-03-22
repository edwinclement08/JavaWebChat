export const hostname = "http://192.168.0.192:8080";

export const SHOW_LOGIN_MODAL = 'SHOW_LOGIN_MODAL';
export const HIDE_LOGIN_MODAL = 'HIDE_LOGIN_MODAL';

export const VERIFY_LOGIN = "VERIFY_LOGIN";
export const LOGIN_VERIFY_SUCCESS = "LOGIN_VERIFY_SUCCESS";
export const LOGIN_VERIFY_FAILURE = "LOGIN_VERIFY_FAILURE";
export const LOGIN_VERIFY_ERROR = "LOGIN_VERIFY_ERROR";

export const USER_SIGNUP = "USER_SIGNUP";
export const SIGNUP_SUCCESSFUL = "SIGNUP_SUCCESSFUL";
export const SIGNUP_FAILURE = "SIGNUP_FAILURE";

export const USER_SIGNOUT = "USER_SIGNOUT";

// export const SAVE_LOGIN_TOKEN = "SAVE_LOGIN_TOKEN"; // TODO

function postData(url = ``, data = {}) {
  // Default options are marked with *
  return fetch(url, {
    method: "POST", // *GET, POST, PUT, DELETE, etc.
    mode: "cors", // no-cors, cors, *same-origin
    cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
    credentials: "same-origin", // include, *same-origin, omit
    headers: {
      "Content-Type": "application/json",
      // "Content-Type": "application/x-www-form-urlencoded",
    },
    redirect: "follow", // manual, *follow, error
    referrer: "no-referrer", // no-referrer, *client
    body: JSON.stringify(data), // body data type must match "Content-Type" header
  })
    .then(response => response.json()); // parses JSON response into native Javascript objects
}


export function displayLoginDialog(status) {
  if (status) {
    return {
      type: SHOW_LOGIN_MODAL,
    };
  } else {
    return {
      type: HIDE_LOGIN_MODAL,
    };
  }
}

let verifyLogin = (username, password) => ({
  type: VERIFY_LOGIN,
  username: username,
  password: password,
});

let loginSuccessful = (username, token) => ({
  type: LOGIN_VERIFY_SUCCESS,
  username: username,
  token: token,
});

let loginFailure = (message) => ({
  type: LOGIN_VERIFY_FAILURE,
  message: message,
});


export function loginVerify(username, password) {
  return dispatch => {
    dispatch(verifyLogin(username, password));
    return postData(hostname + "/rest/user/login/",
      {
        "username": username,
        "password": password
      }
    ).then((result) => {
      if (result.status === "true") {
        setTimeout(() => dispatch(displayLoginDialog(false)), 200);
        return dispatch(loginSuccessful(result.user, result.token));
      } else {
        return dispatch(loginFailure(result.message));
      }
    }, (err) => {
      console.log(err);
      return dispatch({
        type: LOGIN_VERIFY_ERROR
      })
    })
  }
}


let userSignup = (username, password) => ({
  type: USER_SIGNUP,
  username: username,
  password: password,
});

let signupSuccessful = (username, token) => ({
  type: SIGNUP_SUCCESSFUL,
  username: username,
  token: token,
});

let signupFailure = (message) => ({
  type: SIGNUP_FAILURE,
  message: message,
});


export function signupUser(username, password) {
  return dispatch => {
    dispatch(userSignup(username, password));
    return postData(hostname + "/rest/user/register/",
      {
        "username": username,
        "password": password
      }
    ).then((result) => {
      if (result.status === "true") {
        setTimeout(() => dispatch(displayLoginDialog(false)), 200);
        return dispatch(signupSuccessful(result.user, result.token));
      } else {
        return dispatch(signupFailure("Signup Failure"));
      }
    }, (err) => {
      console.log(err);
      return dispatch({
        type: LOGIN_VERIFY_ERROR
      })
    })
  }
}

export function signoutUser() {
  return dispatch => {
    console.log("test signOut");
    return dispatch({
      type: USER_SIGNOUT,
    })
  }
}