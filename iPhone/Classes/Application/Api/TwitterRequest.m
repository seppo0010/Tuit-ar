//
//  TwitterRequest.m
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import "TwitterRequest.h"
#import "Twitter.h"
#import "JSONHTTPRequest.h"

@interface TwitterRequest (private)

+ (NSString*) stringWithOption:(int)_option;

@end


@implementation TwitterRequest

@synthesize success, option;

- (TwitterRequest*) initWithOption:(int)_option andParams:(NSDictionary*)_params andMethod:(int)_method {
	self = [super init];
	option = _option;

	Twitter* twitter = [Twitter getInstance];
	NSString* username = twitter.username;
	buildUrl = [NSMutableString stringWithFormat:@"http%@://", TWITTER_IS_SECURE ? @"s" : @""];
	if (username != NULL) {
		NSString* password = twitter.password;
		if (password != NULL) {
			[buildUrl appendFormat:@"%@:%@@", username, password];
		}
	}
	[buildUrl appendFormat:@"%@%@.json", TWITTER_BASE_URL, [TwitterRequest stringWithOption:option]];
	self.callbackObject = [Twitter getInstance];
	self.successSelector = @selector(finishedRequest:);
	self.failureSelector = @selector(finishedRequest:);
	if (_method == METHOD_GET) {
		if ([_params count] > 0) {
			[buildUrl appendFormat:@"?%@", [HTTPRequest buildQueryStringFromDictionary:_params]];
		}
	} else {
		[self addParameters:_params];
	}
	[buildUrl retain];
	return self;
}

- (void) request {
	[self requestUrl:[NSURL URLWithString:buildUrl]];
}



- (void) callSuccess {
	success = TRUE;
	[super callSuccess];
}
- (void) callFailure {
	success = FALSE;
	[super callFailure];
}

+ (NSString*) stringWithOption:(int)_option {
	switch (_option) {
		case OPTION_CHECK_CREDENTIALS:
			return @"account/verify_credentials";
			break;
		case OPTION_FRIENDS_TIMELINE:
			return @"statuses/home_timeline";
			break;
		case OPTION_REPLIES_TIMELINE:
			return @"statuses/mentions";
			break;
		default:
			[NSException raise:@"Unknown option" format:@"The option %d is unknown", _option];
			break;
	}
	return nil;
}

- (void) dealloc {
	[buildUrl release];
	[super dealloc];
}

@end
