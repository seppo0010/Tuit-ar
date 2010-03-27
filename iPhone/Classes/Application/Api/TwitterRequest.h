//
//  TwitterRequest.h
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JSONHTTPRequest.h"

#define METHOD_GET 0
#define METHOD_POST 1

#define OPTION_CHECK_CREDENTIALS 0
#define OPTION_FRIENDS_TIMELINE 1
#define OPTION_REPLIES_TIMELINE 2
#define OPTION_POST_TWEET 3

@interface TwitterRequest : JSONHTTPRequest {
	NSMutableString* buildUrl;
	int option;
	BOOL success;
}

@property BOOL success;
@property int option;

- (TwitterRequest*) initWithOption:(int)_option andParams:(NSDictionary*)_params andMethod:(int)_method;
- (void) request;

@end
