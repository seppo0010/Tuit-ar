//
//  Preferences.m
//  Tuit-ar
//
//  Created by Seppo on 28/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import "Preferences.h"
#import "Settings.h"

@implementation Preferences

@synthesize preferences;

- (Preferences*) initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
	if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
		preferencesTitles = [[NSArray alloc] initWithObjects:@"Actualizacion Automatica", @"Intervalo de Actualizacion", nil];
	}
	return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
}

- (void) automaticUpdateSwitch {
	[Settings getInstance].automaticUpdate = automaticUpdatesSwitch.on;
}

- (NSInteger)tableView:(UITableView *)table numberOfRowsInSection:(NSInteger)section {
	return [preferencesTitles count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString* identifier = @"preferencesCell";
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];

	if(cell == nil) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:identifier] autorelease];
	}

	[self setCellProperties:cell forPosition:indexPath.row];
	return cell;
}

- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	if (indexPath.row == 0) return nil;
	return indexPath;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	UIAlertView* selectInterval = [[UIAlertView alloc] init];
	selectInterval.title = @"Seleccione la frecuencia";
	selectInterval.delegate = self;
	int freq = [Settings getInstance].updateInterval;
	[selectInterval addButtonWithTitle:[NSString stringWithFormat:@"1 minuto%@", freq == 1 ? @" (actual)" : @""]];
	[selectInterval addButtonWithTitle:[NSString stringWithFormat:@"5 minutos%@", freq == 5 ? @" (actual)" : @""]];
	[selectInterval addButtonWithTitle:@"Cancelar"];
	[selectInterval show];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
	int freq = -1;
	if (buttonIndex == 0) freq = 1;
	else if (buttonIndex == 1) freq = 5;

	if (freq > 0) {
		[Settings getInstance].updateInterval = freq;
	}
	[preferences selectRowAtIndexPath:nil animated:YES scrollPosition:UITableViewScrollPositionNone];
}

- (void) setCellProperties:(UITableViewCell*)cell forPosition:(int)position {
	cell.textLabel.text = [preferencesTitles objectAtIndex:position];
	switch (position) {
		case 0:
			cell.accessoryType = UITableViewCellAccessoryNone;
			if (automaticUpdatesSwitch == nil) {
				automaticUpdatesSwitch = [[[UISwitch alloc] initWithFrame:CGRectMake(216.0, 16.0, 94, 27)] autorelease];
				automaticUpdatesSwitch.on = [Settings getInstance].automaticUpdate;
				[automaticUpdatesSwitch addTarget:self action:@selector(automaticUpdateSwitch) forControlEvents:UIControlEventValueChanged];
			}
			[cell.contentView addSubview:automaticUpdatesSwitch];
			break;
		case 1:
			cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
			break;
	}
}

- (void)dealloc {
	[preferencesTitles release];
    [super dealloc];
}


@end
