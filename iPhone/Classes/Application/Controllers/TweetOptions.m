//
//  TweetOptions.m
//  Tuit-ar
//
//  Created by Seppo on 27/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import "TweetOptions.h"
#import "Tweet.h"
#import "NewTweet.h"

@implementation TweetOptions

@synthesize tweet, replyButton, deleteButton;

- (IBAction) reply {
	NewTweet* newTweet = [[[NewTweet alloc] initWithNibName:@"NewTweet" bundle:nil] autorelease];
	newTweet.replyToUser = tweet.username;
	newTweet.replyToTweetId = tweet.tweet_id;
	[self.navigationController pushViewController:newTweet animated:YES];
}

@end
