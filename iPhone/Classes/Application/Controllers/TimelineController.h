//
//  TimelineController.h
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TimelineObserver.h"

@class Timeline;
@interface TimelineController : UIViewController <UITableViewDelegate, UITableViewDataSource, TimelineObserver> {
	IBOutlet UIActivityIndicatorView* loading;
	IBOutlet UITableView* timeline;
	BOOL visible;
}

@property (assign) UIActivityIndicatorView* loading;
@property (assign) UITableView* timeline;
@property (readonly) NSArray* tweets;

- (Timeline*) timelineModel;
- (void) refresh;
- (void) showLoading;
- (void) hideLoading;

@end
