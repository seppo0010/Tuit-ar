//
//  TwitterObserver.h
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@class TwitterRequest;

@protocol TwitterObserver <NSObject>

- (void) requestHasFinished:(TwitterRequest*)request;
- (void) requestHasStarted:(TwitterRequest*)request;

@end
