//
//  Settings.h
//  Tuit-ar
//
//  Created by Seppo on 28/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SettingsObserver.h"

#define UPDATE_INTERVAL @"updateInterval"
#define AUTOMATIC_UPDATE @"automaticUpdate"
#define UPDATE_INTERVAL_DEFAULT 5
#define AUTOMATIC_UPDATE_DEFAULT TRUE

@interface Settings : NSObject {
	NSMutableSet* observers;
}

@property BOOL automaticUpdate;
@property int updateInterval;

+ (Settings*) getInstance;
- (void) addObserver:(id<SettingsObserver>)observer;
- (void) removeObserver:(id<SettingsObserver>)observer;
- (void) callObservers;

@end
