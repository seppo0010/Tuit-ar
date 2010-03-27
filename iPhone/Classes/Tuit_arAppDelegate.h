//
//  Tuit_arAppDelegate.h
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright Apple Inc 2010. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Tuit_arAppDelegate : NSObject <UIApplicationDelegate> {
    UIWindow *window;

	UIViewController* controller;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;

@end

