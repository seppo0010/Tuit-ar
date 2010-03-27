//
//  TimelineCell.h
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@class Tweet;
@interface TimelineCell : UITableViewCell {
	IBOutlet UILabel* username;
	IBOutlet UILabel* message;
	IBOutlet UILabel* date;
	Tweet* tweet;
}

@property (assign) UILabel* username;
@property (assign) UILabel* message;
@property (assign) UILabel* date;
@property (retain) Tweet* tweet;

@end
