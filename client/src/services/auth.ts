import httpClient from "./httpClient";
import _ from "lodash";

// const BASE = "http://localhost:8080";
// const URL = BASE + '/oauth2/authorization/github';
const URL = '/oauth2/authorization/github';
// const URL = '/loggedUser';

export const signInGitHubService = async (): Promise<null> => {
    // await fetch(URL)
    const res = await httpClient.get(URL);
    // console.log(res);
    return null;
    // return _.get(res, 'data', null);
}

