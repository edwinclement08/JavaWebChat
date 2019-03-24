import {combineReducers} from "redux";
import loginReducer from "./login";
import screenReducer from "./screen";


const reducer = combineReducers({
  login: loginReducer,
  screen: screenReducer,
});
export default reducer;