import * as ScreenActions from "../actions/screen";
import produce from "immer";

function screenReducer(state, action) {
  if (typeof state === 'undefined') {
    return {
      visibleScreen: "home",
      details: {
        "home": {
          users: [],
          friends: [],    // username of chat ppl
          chats: {}        // chats with all the ppl
        }
      },
    }
  }
  switch (action.type) {
    case ScreenActions.SHOW_SCREEN:
      return {...state, visibleScreen: action.name};
    case ScreenActions.LOAD_USER_FRIENDS_SUCCESS:
      return produce(state, (draft) => {
        console.log(action.friends)
        draft.details.home.friends = action.friends;
        return draft
      });
    case ScreenActions.LOAD_USER_FRIEND_ALL_MESSAGE_FINISH:
      let newState = {
        ...state,
        details: {...state.details, home: {...state.details.home, chats: {...state.details.home.chats}}}
      };
      newState.details.home.chats[action.friend] = action.contents;
      return newState;
    case ScreenActions.SEND_MESSAGE_SUCCESS:
      return produce(state, (draft) => {
        // draft.details.home.chats[action.receiver].push(action.contents);
        return draft;
      });
    case ScreenActions.LOAD_ALL_USERS_SUCCESS:
      return produce(state, (draft) => {
        draft.details.home.users = action.users;
        return draft;
      });
    case ScreenActions.CHAT_SENT:
      return produce(state, (draft) => {
        draft.details.home.users = action.users;
        return draft;
      });
    default:
      return state;
  }
}

export default screenReducer;