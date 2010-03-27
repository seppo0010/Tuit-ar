//
//  NewTweet.m
//  Tuit-ar
//
//  Created by Seppo on 27/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import "NewTweet.h"
#import "Twitter.h"
#import "TwitterRequest.h"

@implementation NewTweet

@synthesize username, messageField, send, replyToUser, replyToTweetId;

- (void) viewWillAppear:(BOOL)animated {
	[super viewWillAppear:animated];
	[[Twitter getInstance] addObserver:self];
}

- (IBAction) submit {
	NSMutableDictionary* params = [NSMutableDictionary dictionaryWithObjectsAndKeys:
							messageField.text, @"status",
							nil
								   ];
	if (replyToTweetId != nil) [params setValue:replyToTweetId forKey:@"in_reply_to_status_id"];
	[[Twitter getInstance] requestUrl:OPTION_POST_TWEET withParams:params andMethod:METHOD_POST];
	[messageField resignFirstResponder];
}

- (void) requestHasStarted:(TwitterRequest*)request {
	if (request.option != OPTION_POST_TWEET) return;
	[self showLoading];
}

- (void) requestHasFinished:(TwitterRequest*)request {
	if (request.option != OPTION_POST_TWEET) return;
	[self hideLoading];
	if (request.success == FALSE) {
		[[[[UIAlertView alloc] initWithTitle:nil message:@"No se pudo enviar el mensaje" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] autorelease] show];
	} else {
		[[[[UIAlertView alloc] initWithTitle:nil message:@"Mensaje enviado" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] autorelease] show];
		messageField.text = @"";
	}
}

- (void) showLoading {
}

- (void) hideLoading {
}

@end
