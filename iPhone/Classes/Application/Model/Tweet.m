//
//  Tweet.m
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import "Tweet.h"


@implementation Tweet

@synthesize tweet_id, username, message, date;

- (Tweet*)initWithDictionary:(NSDictionary*)dictionary {
	self = [super init];
	self.tweet_id = [dictionary valueForKey:@"id"];
	self.username = [[dictionary valueForKey:@"user"] valueForKey:@"screen_name"];
	self.message = [dictionary valueForKey:@"text"];

	NSDateFormatter* dateFormatter = [[NSDateFormatter alloc] init];
	[dateFormatter setDateFormat:@"EEE MMM d HH:mm:ss Z yyyy"];
	NSLocale* locale = [[NSLocale alloc] initWithLocaleIdentifier:@"US"];
	[dateFormatter setLocale:locale];
	self.date = [dateFormatter dateFromString:[dictionary valueForKey:@"created_at"]];
	NSLog(@"%@: %d", [dictionary valueForKey:@"created_at"], [date timeIntervalSinceNow]);
	[dateFormatter release];
	[locale release];

	return self;
}

+ (NSString*) calculateElapsed:(NSDate*)date {
	int ageInSeconds = -[date timeIntervalSinceNow];
	
	if (ageInSeconds <= 0) {
		return @"just now";
	}
	if (ageInSeconds < 60) {
		int n = ageInSeconds;
		return [NSString stringWithFormat:@"about %d second%@ ago", n, (n > 1 ? @"s" : @"")];
	}
	if (ageInSeconds < 60 * 60) {
		int n = (int) floor(ageInSeconds/60);
		return [NSString stringWithFormat:@"about %d minute%@ ago", n, (n > 1 ? @"s" : @"")];
	}
	if (ageInSeconds < 60 * 60 * 24) {
		int n = (int) floor(ageInSeconds/60/60);
		return [NSString stringWithFormat:@"about %d hour%@ ago", n, (n > 1 ? @"s" : @"")];
	}
	if (ageInSeconds < 60 * 60 * 24 * 7) {
		int n = (int) floor(ageInSeconds/60/60/24);
		return [NSString stringWithFormat:@"about %d day%@ ago", n, (n > 1 ? @"s" : @"")];
	}
	if (ageInSeconds < 60 * 60 * 24 * 31) {
		int n = (int) floor(ageInSeconds/60/60/24/7);
		return [NSString stringWithFormat:@"about %d week%@ ago", n, (n > 1 ? @"s" : @"")];
	}
	if (ageInSeconds < 60 * 60 * 24 * 365) {
		int n = (int) floor(ageInSeconds/60/60/24/31);
		return [NSString stringWithFormat:@"about %d month%@ ago", n, (n > 1 ? @"s" : @"")];
	}
	int n = (int)floor(ageInSeconds/60/60/24/365);
	return [NSString stringWithFormat:@"about %d year%@ ago", n, (n > 1 ? @"s" : @"")];
}

@end
