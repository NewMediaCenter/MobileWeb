//
//  ChildBrowserViewController.h
//
//  Created by Jesse MacFadyen on 21/07/09.
//  Copyright 2009 Nitobi. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol ChildBrowserDelegate<NSObject>

-(void) onChildLocationChange:(NSString*)newLoc;
-(void) onOpenInSafari;
-(void) onClose;

@end


@interface ChildBrowserViewController : UIViewController < UIWebViewDelegate > {
    IBOutlet UIToolbar *toolbar;
	IBOutlet UIWebView *webView;
	IBOutlet UIBarButtonItem *refreshBtn;
	IBOutlet UIBarButtonItem *backBtn;
	IBOutlet UIBarButtonItem *fwdBtn;
	IBOutlet UIBarButtonItem *safariBtn;
	BOOL scaleEnabled;
	BOOL isImage;
	NSString *imageURL;
	NSArray *supportedOrientations;
	id <ChildBrowserDelegate> delegate;
}

@property (nonatomic, retain, readwrite) id <ChildBrowserDelegate> delegate;
@property (nonatomic, retain, readwrite) NSArray *supportedOrientations;
@property (nonatomic, retain, readwrite) NSString *imageURL;
@property (nonatomic, assign, readwrite) BOOL isImage;
@property (nonatomic, retain, readwrite) UIToolbar *toolbar;
@property (nonatomic, retain, readwrite) UIWebView *webView;
@property (nonatomic, retain, readwrite) UIBarButtonItem *refreshBtn;
@property (nonatomic, retain, readwrite) UIBarButtonItem *backBtn;
@property (nonatomic, retain, readwrite) UIBarButtonItem *fwdBtn;
@property (nonatomic, retain, readwrite) UIBarButtonItem *safariBtn;


- (ChildBrowserViewController *) initWithScale:(BOOL)enabled;

- (BOOL) shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation; 
- (IBAction) onDoneButtonPress:(id)sender;
- (IBAction) onSafariButtonPress:(id)sender;
- (void) loadURL:(NSString *)url;
- (void) closeBrowser;

@end
