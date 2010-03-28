//
//  Login.m
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import "Login.h"
#import "Twitter.h"
#import "TwitterRequest.h"
#import "TimelineFriendsController.h"
#import "MainController.h"

@implementation Login

@synthesize username, password, loginButton, loading;

- (void)viewDidLoad {
    [super viewDidLoad];
	self.title = @"Ingresar";
	[[Twitter getInstance] addObserver:self];
}

- (IBAction) goToPassword {
	[password becomeFirstResponder];
}

- (IBAction) login {
	username.enabled = FALSE;
	password.enabled = FALSE;
	loginButton.enabled = FALSE;
	Twitter* twitter = [Twitter getInstance];
	twitter.username = username.text;
	twitter.password = password.text;
	[twitter requestUrl:OPTION_CHECK_CREDENTIALS];
}

- (void) loginFailed {
	Twitter* twitter = [Twitter getInstance];
	[twitter clearCredentials];
	username.enabled = TRUE;
	password.enabled = TRUE;
	loginButton.enabled = TRUE;
	[[[[UIAlertView alloc] initWithTitle:nil message:@"No se ha podido verificar su cuenta" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] autorelease] show];
}

- (void) requestHasStarted:(TwitterRequest*)request {
	[self showLoading];
}

- (void) requestHasFinished:(TwitterRequest*)request {
	[self hideLoading];
	if (request.option == OPTION_CHECK_CREDENTIALS) {
		if (request.success) {
			[[MainController getInstance] dismissLogin];
		} else {
			[self loginFailed];
		}
	}
}

- (void) showLoading {
	loading.hidden = NO;
	[loading startAnimating];
}

- (void) hideLoading {
	loading.hidden = YES;
	[loading stopAnimating];
}

- (void) dealloc {
	[[Twitter getInstance] removeObserver:self];
	[super dealloc];
}

@end
