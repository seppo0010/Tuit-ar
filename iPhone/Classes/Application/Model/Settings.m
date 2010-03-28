//
//  Settings.m
//  Tuit-ar
//
//  Created by Seppo on 28/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import "Settings.h"
#import "NSMutableSetNonRetain.h"

@implementation Settings

static Settings* instance = NULL;
+ (Settings*) getInstance {
	if (instance == NULL) instance = [[Settings alloc] init];
	return instance;
}

- (Settings*) init {
	if (self = [super init]) {
		observers = [[NSMutableSet setNonRetaining] retain];
	}
	return self;
}

- (BOOL) automaticUpdate {
	id _automaticUpdate = [[NSUserDefaults standardUserDefaults] objectForKey:AUTOMATIC_UPDATE];
	if (_automaticUpdate == nil) return AUTOMATIC_UPDATE_DEFAULT;
	return [_automaticUpdate boolValue];
}

- (void) setAutomaticUpdate:(BOOL)_automaticUpdate {
	[[NSUserDefaults standardUserDefaults] setObject:[NSNumber numberWithBool:_automaticUpdate] forKey:AUTOMATIC_UPDATE];
	[self callObservers];
}

- (int) updateInterval {
	id _updateIterval = [[NSUserDefaults standardUserDefaults] objectForKey:UPDATE_INTERVAL];
	if (_updateIterval == nil) return UPDATE_INTERVAL_DEFAULT;
	return [_updateIterval intValue];
}

- (void) setUpdateInterval:(int)_updateIterval {
	[[NSUserDefaults standardUserDefaults] setObject:[NSNumber numberWithInt:_updateIterval] forKey:UPDATE_INTERVAL];
	[self callObservers];
}

- (void) addObserver:(id<SettingsObserver>)observer {
	[observers addObject:observer];
}

- (void) removeObserver:(id<SettingsObserver>)observer {
	[observers removeObject:observer];
}

- (void) callObservers {
	NSMutableSet* _observers = [observers copy];
	[_observers makeObjectsPerformSelector:@selector(settingsHasChanged:) withObject:self];
	[_observers release];
}

@end
