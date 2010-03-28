//
//  NewTweet.h
//  Tuit-ar
//
//  Created by Seppo on 27/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TwitterObserver.h"

@interface NewTweet : UIViewController <TwitterObserver> {
	IBOutlet UILabel* username;
	IBOutlet UITextView* messageField;
	IBOutlet UIButton* send;
	IBOutlet UIActivityIndicatorView* loading;

	NSString* replyToUser;
	NSString* replyToTweetId;
}

@property (retain) NSString* replyToUser;
@property (retain) NSString* replyToTweetId;
@property (assign) UILabel* username;
@property (assign) UITextView* messageField;
@property (assign) UIButton* send;
@property (assign) UIActivityIndicatorView* loading;

- (IBAction) submit;
- (void) showLoading;
- (void) hideLoading;

@end
