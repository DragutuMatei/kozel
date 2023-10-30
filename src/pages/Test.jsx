import React, { useState } from 'react';

const TwitterChecker = () => {
  const [username, setUsername] = useState('');
  const [tweetId, setTweetId] = useState('');
  const [liked, setLiked] = useState(null);

  const checkLikeStatus = async () => {
    try {
      const response = await fetch(
        `https://api.twitter.com/2/tweets/${tweetId}/liking_users`,
        {
          headers: {
            Authorization: `Bearer AAAAAAAAAAAAAAAAAAAAAPwVqwEAAAAA%2BH%2Fh46WtuAIrPJnNKbxjHUjcDcU%3DdmGY3lBnGOLHHJpwqYiuw7CK9IlBbIeRVQg6icd1kUsb1yQKvD`,
          },
        }
      );

      const data = await response.json();

      // Check if the username is in the list of liking users
      const userLiked = data.data.some(
        (user) => user.username === username.toLowerCase()
      );

      setLiked(userLiked);
    } catch (error) {
      console.error('Error checking like status:', error);
    }
  };

  return (
    <div>
      <label>
        Twitter Username:
        <input
          type="text"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
      </label>
      <br />
      <label>
        Tweet ID:
        <input
          type="text"
          value={tweetId}
          onChange={(e) => setTweetId(e.target.value)}
        />
      </label>
      <br />
      <button onClick={checkLikeStatus}>Check Like Status</button>
      <br />
      {liked === null ? null : liked ? (
        <p>{`${username} liked the tweet!`}</p>
      ) : (
        <p>{`${username} did not like the tweet.`}</p>
      )}
    </div>
  );
};

export default TwitterChecker;
