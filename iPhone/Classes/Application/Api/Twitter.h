//
//  Twitter.h
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "TwitterObserver.h"

#define TWITTER_IS_SECURE FALSE
#define TWITTER_BASE_URL @"boludeo.delapalo.net/api/"

@interface Twitter : NSObject {
	NSMutableSet* observers;
	NSString* username;
	NSString* password;
}

@property (retain) NSString* username;
@property (retain) NSString* password;

+ (Twitter*) getInstance;
- (void) requestUrl:(int)_option;
- (void) requestUrl:(int)_option withParams:(NSDictionary*)_params andMethod:(int)_method;
- (void) clearCredentials;
- (void) addObserver:(id<TwitterObserver>)_observer;
- (void) removeObserver:(id<TwitterObserver>)_observer;

@end
