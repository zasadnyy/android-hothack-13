package ua.org.gdg.devfest.droidconsched.io.model;

import ua.org.gdg.devfest.droidconsched.io.ServerArrayResponse;

import java.util.List;

/**
 * TracksResponse as modelled in the Google Developers API.
 *
 * This is an implementation which replaces the generated version which shipped with IOsched and
 * makes the code human more human readable and removed the dependence on additional Google libraries.
 */
public class TweetsResponse extends ServerArrayResponse<TweetResponse> {

    public List<TweetResponse> getTweets()
    {
        return getArrayContents();
    }

    @Override
    protected String getArrayAttributeName() {
        return "tweets";
    }

    @Override
    protected TweetResponse getNewObject() {
        return new TweetResponse();
    }
}