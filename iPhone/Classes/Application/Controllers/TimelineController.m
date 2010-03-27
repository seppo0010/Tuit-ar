//
//  TimelineController.m
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import "TimelineController.h"
#import "Timeline.h"
#import "TimelineCell.h"
#import "UITableViewCellLoader.h"

@implementation TimelineController

@synthesize timeline;

- (TimelineController*)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if ((self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil])) {
		Timeline* _timeline = [self timelineModel];
        [_timeline addObserver:self];
    }
    return self;
}

- (void)viewWillAppear:(BOOL)animated {
	[super viewWillAppear:animated];
	visible = TRUE;
	[timeline reloadData];
	if ([self.tweets count] == 0) [self refresh];
}

- (void) viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
	visible = FALSE;
}

- (Timeline*) timelineModel {
	return nil;
}

- (NSArray*) tweets {
	return [self timelineModel].tweets;
}

- (NSInteger)tableView:(UITableView *)table numberOfRowsInSection:(NSInteger)section {
	return [self.tweets count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	TimelineCell* cell = (TimelineCell*)[TimelineCell loadCellNibForTableView:tableView];
	cell.tweet = [self.tweets objectAtIndex:indexPath.row];
	return cell;
}

- (void) timelineRequestStarted:(Timeline*)_timeline {
	[self showLoading];
}

- (void) timelineUpdateHasFailed:(Timeline*)_timeline {
	[[[[UIAlertView alloc] initWithTitle:nil message:@"No se pudo actualizar la lista de mensaje" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] autorelease] show];
}

- (void) timelineRequestFinished:(Timeline*)_timeline {
	[self hideLoading];
}

- (void) timelineHasChanged:(Timeline*)_timeline {
	[timeline reloadData];
}

- (void) showLoading {
}

- (void) hideLoading {
}

- (void) refresh {
	[[self timelineModel] refresh];
}

- (void) dealloc {
	[[self timelineModel] removeObserver:self];
	[super dealloc];
}

@end
