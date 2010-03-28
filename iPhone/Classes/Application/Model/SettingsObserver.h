//
//  SettingsObserver.h
//  Tuit-ar
//
//  Created by Seppo on 28/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@class Settings;
@protocol SettingsObserver

- (void) settingsHasChanged:(Settings*)settings;

@end
