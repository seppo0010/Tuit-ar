//
//  MainController.h
//  Tuit-ar
//
//  Created by Seppo on 27/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@class Login;
@interface MainController : UITabBarController {
	Login* login;
}


+ (MainController*) getInstance;
- (void) dismissLogin;

@end
