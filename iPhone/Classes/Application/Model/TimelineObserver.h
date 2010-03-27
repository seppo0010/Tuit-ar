//
//  TimelineObserver.h
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@class Timeline;
@protocol TimelineObserver

- (void) timelineRequestStarted:(Timeline*)timeline;
- (void) timelineUpdateHasFailed:(Timeline*)timeline;
- (void) timelineRequestFinished:(Timeline*)timeline;
- (void) timelineHasChanged:(Timeline*)timeline;

@end
