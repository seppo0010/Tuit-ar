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

@implementation Login

@synthesize username, password, loginButton;

- (void)viewDidLoad {
    [super viewDidLoad];
	self.title = @"Ingresar";
}

- (void)viewWillAppear:(BOOL)animated {
	[super viewWillAppear:animated];
	[[Twitter getInstance] addObserver:self];
}

- (void)viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
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
		} else {
			[self loginFailed];
		}
	}
}

- (void) showLoading {
	
}

- (void) hideLoading {
	
}


@end
