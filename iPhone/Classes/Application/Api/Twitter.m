//
//  Twitter.m
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import "Twitter.h"
#import "TwitterRequest.h"
#import "NSMutableSetNonRetain.h"

@interface Twitter (private)

- (void) startedRequest:(TwitterRequest*)_request;
- (void) finishedRequest:(TwitterRequest*)_request;

@end

@implementation Twitter

@synthesize username, password;

static Twitter* instance = NULL;
+ (Twitter*) getInstance {
	if (instance == NULL) instance = [[Twitter alloc] init];
	return instance;
}

- (Twitter*)init {
	self = [super init];
	observers = [[NSMutableSet setNonRetaining] retain];
	return self;
}

- (void) requestUrl:(int)_option {
	[self requestUrl:_option withParams:NULL andMethod:METHOD_GET];
}

- (void) requestUrl:(int)_option withParams:(NSDictionary*)_params andMethod:(int)_method {
	TwitterRequest* request = [[[TwitterRequest alloc] initWithOption:_option andParams:_params andMethod:_method] autorelease];
	[request request];
	[self startedRequest:request];
}

- (void) clearCredentials {
	self.username = nil;
	self.password = nil;
}

- (void) addObserver:(id<TwitterObserver>)_observer {
	[observers addObject:_observer];
}

- (void) removeObserver:(id<TwitterObserver>)_observer {
	[observers removeObject:_observer];
}

- (void) startedRequest:(TwitterRequest*)_request {
	[observers makeObjectsPerformSelector:@selector(requestHasStarted:) withObject:_request];
}

- (void) finishedRequest:(TwitterRequest*)_request {
	[observers makeObjectsPerformSelector:@selector(requestHasFinished:) withObject:_request];
}

@end
