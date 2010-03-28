//
//  Preferences.h
//  Tuit-ar
//
//  Created by Seppo on 28/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface Preferences : UIViewController <UITableViewDelegate, UITableViewDataSource> {
	IBOutlet UITableView* preferences;

	NSArray* preferencesTitles;
	UISwitch* automaticUpdatesSwitch;
}

@property (assign) UITableView* preferences;

- (void) setCellProperties:(UITableViewCell*)cell forPosition:(int)position;

@end
