//
//  Created by Jesse MacFadyen on 10-05-29.
//  Copyright 2010 Nitobi. All rights reserved.
//

#import "ChildBrowserCommand.h"

#ifdef PHONEGAP_FRAMEWORK
#import <PhoneGap/PhoneGapViewController.h>
#else
#import "PhoneGapViewController.h"
#endif


@implementation ChildBrowserCommand

@synthesize childBrowser;

- (void) dealloc {
    [self setChildBrowser:nil];
	[super dealloc];
}

- (void) showWebPage:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options {	
	if (childBrowser == NULL) {
		childBrowser = [[ChildBrowserViewController alloc] initWithScale:FALSE];
		[childBrowser setDelegate:self];
	}
    
	PhoneGapViewController *cont = (PhoneGapViewController *)[super appViewController];
	[childBrowser setSupportedOrientations:[cont supportedOrientations]];
	[cont presentModalViewController:childBrowser animated:YES];
	
	NSString *url = (NSString *)[arguments objectAtIndex:0];
	    
	[childBrowser loadURL:url];
}

-(void) close:(NSMutableArray *)arguments withDict:(NSMutableDictionary *)options {
	[childBrowser closeBrowser];	
}

-(void) onClose {
	NSString *jsCallback = [NSString stringWithFormat:@"ChildBrowser._onClose();",@""];
	[[self webView] stringByEvaluatingJavaScriptFromString:jsCallback];
}

-(void) onOpenInSafari {
	NSString *jsCallback = [NSString stringWithFormat:@"ChildBrowser._onOpenExternal();",@""];
	[[self webView] stringByEvaluatingJavaScriptFromString:jsCallback];
}


-(void) onChildLocationChange:(NSString*)newLoc {	
	NSString *tempLoc = [NSString stringWithFormat:@"%@",newLoc];
	NSString *encUrl = [tempLoc stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
	NSString *jsCallback = [NSString stringWithFormat:@"ChildBrowser._onLocationChange('%@');",encUrl];
	[[self webView] stringByEvaluatingJavaScriptFromString:jsCallback];
}

@end
