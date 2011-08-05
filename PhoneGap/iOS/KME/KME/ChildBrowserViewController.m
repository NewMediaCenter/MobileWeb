//
//  ChildBrowserViewController.m
//
//  Created by Jesse MacFadyen on 21/07/09.
//  Copyright 2009 Nitobi. All rights reserved.
//

#import "ChildBrowserViewController.h"


@implementation ChildBrowserViewController

@synthesize imageURL;
@synthesize supportedOrientations;
@synthesize isImage;
@synthesize delegate;
@synthesize toolbar;
@synthesize webView;
@synthesize backBtn;
@synthesize fwdBtn;
@synthesize safariBtn;
@synthesize refreshBtn;

- (void) dealloc {
	[[self webView] setDelegate:nil];
	 
	[self setWebView:nil];
	[self setRefreshBtn:nil];
	[self setBackBtn:nil];
	[self setFwdBtn:nil];
	[self setSafariBtn:nil];
	[self setSupportedOrientations:nil];
    
	[super dealloc];
}

+ (NSString*) resolveImageResource:(NSString*)resource {
	NSString* systemVersion = [[UIDevice currentDevice] systemVersion];
	BOOL isLessThaniOS4 = ([systemVersion compare:@"4.0" options:NSNumericSearch] == NSOrderedAscending);
	
	if (isLessThaniOS4) {
        return [NSString stringWithFormat:@"%@.png", resource];
	}
	
	return resource;
}


- (ChildBrowserViewController *) initWithScale:(BOOL)enabled {
    self = [super init];
	scaleEnabled = enabled;	
	return self;	
}

- (void) viewDidLoad {
    [super viewDidLoad];
    
	[refreshBtn setImage:[UIImage imageNamed:[[self class] resolveImageResource:@"ChildBrowser.bundle/but_refresh"]]];
    [backBtn    setImage:[UIImage imageNamed:[[self class] resolveImageResource:@"ChildBrowser.bundle/arrow_left"]]];
	[fwdBtn     setImage:[UIImage imageNamed:[[self class] resolveImageResource:@"ChildBrowser.bundle/arrow_right"]]];
	[safariBtn  setImage:[UIImage imageNamed:[[self class] resolveImageResource:@"ChildBrowser.bundle/compass"]]];
    
	[webView setDelegate:self];
	[webView setScalesPageToFit:YES];
	[webView setBackgroundColor:[UIColor whiteColor]];
    
    [toolbar setHidden:YES];    
}

- (void) didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (void) viewDidUnload {}

- (void) closeBrowser {	
	if (delegate != NULL) {
		[delegate onClose];		
	}
	
	[[super parentViewController] dismissModalViewControllerAnimated:YES];
}

- (IBAction) onDoneButtonPress:(id)sender {
	[self closeBrowser];
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:@"about:blank"]];
    [webView loadRequest:request];
}


- (IBAction) onSafariButtonPress:(id)sender {
	if (delegate != NULL) {
		[delegate onOpenInSafari];		
	}
	
	if (isImage) {
		NSURL* pURL = [[NSURL alloc] initWithString:imageURL];
		[[UIApplication sharedApplication] openURL:pURL];
	} else {
		NSURLRequest *request = webView.request;
		[[UIApplication sharedApplication] openURL:request.URL];
	}
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation) interfaceOrientation  {
	BOOL autoRotate = [[self supportedOrientations] count] > 1; 

	if (autoRotate) {
		if ([[self supportedOrientations] containsObject:[NSNumber numberWithInt:interfaceOrientation]]) {
			return YES;
		}
    }
	
	return NO;
}

- (void) loadURL:(NSString*)url {    
	if ([url hasSuffix:@".png"]  || 
        [url hasSuffix:@".jpg"]  || 
        [url hasSuffix:@".jpeg"] || 
        [url hasSuffix:@".bmp"]  || 
        [url hasSuffix:@".gif"]) {
        
		[imageURL release];
		imageURL = [url copy];
		
        isImage = YES;
		
        NSString* htmlText = @"<html><body style='background-color:#333;margin:0px;padding:0px;'><img style='min-height:200px;margin:0px;padding:0px;width:100%;height:auto;' alt='' src='IMGSRC'/></body></html>";
		htmlText = [htmlText stringByReplacingOccurrencesOfString:@"IMGSRC" withString:url];
        
		[webView loadHTMLString:htmlText baseURL:[NSURL URLWithString:@""]];
	} else {
		imageURL = @"";
		isImage = NO;
		NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:url]];
        [webView loadRequest:request];
	}
	
    [webView setHidden:NO];
}


- (void)webViewDidStartLoad:(UIWebView *)sender {
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];

	[backBtn setEnabled:[webView canGoBack]];
    [fwdBtn setEnabled:[webView canGoForward]];	
}

- (void)webViewDidFinishLoad:(UIWebView *)sender {
	[backBtn setEnabled:[webView canGoBack]];
	[fwdBtn setEnabled:webView.canGoForward];
    
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
        
	NSURLRequest *request = [webView request];
    NSRange range = [[[request URL] path] rangeOfString:@"kme-"];
    NSRange range2 = [[[request URL] absoluteString] rangeOfString:@"/resources?resId"];
    
    if (range.location != NSNotFound && range2.location == NSNotFound) {
        if (![toolbar isHidden]) {
            [webView setFrame:CGRectMake(0, 0, webView.frame.size.width, webView.frame.size.height + toolbar.frame.size.height)];
            [toolbar setHidden:YES];
        }
    } else {
        if ([toolbar isHidden]) {
            [toolbar setHidden:NO];
            [webView setFrame:CGRectMake(0, 0, webView.frame.size.width, webView.frame.size.height - toolbar.frame.size.height)];                
        }
    }
    
	if(delegate != NULL) {
		[delegate onChildLocationChange:request.URL.absoluteString];		
	}    
}


@end
