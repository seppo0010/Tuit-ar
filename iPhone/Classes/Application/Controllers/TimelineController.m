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
#import "TweetOptions.h"

@implementation TimelineController

@synthesize timeline, loading;

- (TimelineController*)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if ((self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil])) {
		Timeline* _timeline = [self timelineModel];
        [_timeline addObserver:self];
    }
    return self;
}

- (void) viewDidLoad {
	[super viewDidLoad];
	self.navigationItem.leftBarButtonItem = [[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemRefresh target:self action:@selector(refresh)] autorelease];
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

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	TweetOptions* options = [[[TweetOptions alloc] initWithNibName:@"TweetOptions" bundle:nil] autorelease];
	options.tweet = [self.tweets objectAtIndex:indexPath.row];
	[self.navigationController pushViewController:options animated:YES];
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
	loading.hidden = NO;
	[loading startAnimating];
}

- (void) hideLoading {
	loading.hidden = YES;
	[loading stopAnimating];
}

- (void) refresh {
	[[self timelineModel] refresh];
}

- (void) dealloc {
	[[self timelineModel] removeObserver:self];
	[super dealloc];
}

@end
