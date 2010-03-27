//
//  Login.h
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TwitterObserver.h"

@interface Login : UIViewController <TwitterObserver> {
	IBOutlet UITextField* username;
	IBOutlet UITextField* password;
	IBOutlet UIButton* loginButton;
}

@property (assign) UITextField* username;
@property (assign) UITextField* password;
@property (assign) UIButton* loginButton;

- (IBAction) goToPassword;
- (IBAction) login;
- (void) showLoading;
- (void) hideLoading;
- (void) loginFailed;

@end
