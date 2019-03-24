import * as ScreenActions from "../actions/screen";


function screenReducer(state, action) {
  if (typeof state === 'undefined') {
    return {
      visibleScreen: "home",
      details: {
        "home": {
          friends: []    // username of chat ppl
        }
      },
    }
  }
  switch (action.type) {
    case ScreenActions.SHOW_SCREEN:
      return {...state, visibleScreen: action.name};
    case ScreenActions.LOAD_USER_FRIENDS_SUCCESS:
      return {...state, details: {"home": {friends: action.friends}}};
    default:
      return state;
  }
}

export default screenReducer;