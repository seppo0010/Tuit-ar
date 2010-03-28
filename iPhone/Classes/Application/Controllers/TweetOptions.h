//
//  TweetOptions.h
//  Tuit-ar
//
//  Created by Seppo on 27/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@class Tweet;
@interface TweetOptions : UIViewController {
	IBOutlet UIButton* replyButton;
	IBOutlet UIButton* deleteButton;
	Tweet* tweet;
}

@property (assign) UIButton* replyButton;
@property (assign) UIButton* deleteButton;
@property (assign) Tweet* tweet;

- (IBAction) reply;

@end
