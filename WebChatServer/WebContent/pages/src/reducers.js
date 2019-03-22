import {combineReducers} from 'redux'
import {
  SHOW_LOGIN_MODAL, HIDE_LOGIN_MODAL, USER_SIGNOUT,
  LOGIN_VERIFY_SUCCESS, LOGIN_VERIFY_FAILURE, SIGNUP_SUCCESSFUL, SIGNUP_FAILURE
} from "./actions";


function loginReducer(state, action) {
  if (typeof state === 'undefined') {
    return {
      userData: {
        loggedIn: false,
        user: "",
        token: "",
      },
      loginDialog: {
        isLoginDialogVisible: false,
        loginDialogMessage: "",
        isSuccess: false,
      }
    }
  }
  let newloginDialog, newUserData;

  switch (action.type) {
    case SHOW_LOGIN_MODAL:
      newloginDialog = Object.assign({}, state.loginDialog, {isLoginDialogVisible: true});
      return {...state, ...{loginDialog: newloginDialog}};
    case HIDE_LOGIN_MODAL:
      newloginDialog = Object.assign({}, state.loginDialog, {isLoginDialogVisible: false});
      return {...state, ...{loginDialog: newloginDialog}};
    case LOGIN_VERIFY_SUCCESS:
      newloginDialog = Object.assign({}, state.loginDialog,
        {loginDialogMessage: "Login Success", isSuccess: true});
      newUserData = {
        loggedIn: true,
        user: action.username,
        token: action.token,
      };
      return {...state, ...{loginDialog: newloginDialog, userData: newUserData}};
    case LOGIN_VERIFY_FAILURE:
      newloginDialog = Object.assign({}, state.loginDialog,
        {loginDialogMessage: action.message, isSuccess: false});
      return {...state, ...{loginDialog: newloginDialog}};
    case SIGNUP_SUCCESSFUL:
      newUserData = {
        loggedIn: true,
        user: action.username,
        token: action.token,
      };
      newloginDialog = Object.assign({}, state.loginDialog,
        {loginDialogMessage: "Signup Success", isSuccess: true});
      return {...state, ...{loginDialog: newloginDialog, userData: newUserData}};
    case SIGNUP_FAILURE:
      newloginDialog = Object.assign({}, state.loginDialog,
        {loginDialogMessage: action.message, isSuccess: false});
      return {...state, ...{loginDialog: newloginDialog}};
    case USER_SIGNOUT:
      newUserData = Object.assign({}, state.userData,
        {
          loggedIn: false,
          user: "",
          token: "",
        });
      return {...state, ...{userData: newUserData}};
    default:
      return state;
  }
}

const reducer = combineReducers({
  login: loginReducer,
});

export default reducer;

