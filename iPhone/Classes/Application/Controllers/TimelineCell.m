//
//  TimelineCell.m
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import "TimelineCell.h"
#import "Tweet.h"

@implementation TimelineCell

@synthesize username, message, date, tweet;

- (void) setTweet:(Tweet *)_tweet {
	if (_tweet == tweet) return;
	[tweet release];
	tweet = [_tweet retain];
	self.username.text = tweet.username;
	self.message.text = tweet.message;
	self.date.text = [Tweet calculateElapsed:tweet.date];
}

@end
