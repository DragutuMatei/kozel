export const isRegisterWithTwitter = () => {
  if (
    localStorage.getItem("oauth_token") != null &&
    localStorage.getItem("oauth_token_secret") != null
  )
    return true;
  return false;
};
