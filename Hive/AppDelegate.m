//
//  AppDelegate.m
//  Hive
//
//  Created by Denis Kubasov on 14/02/15.
//  Copyright (c) 2015 Denis Kubasov. All rights reserved.
//

#import "AppDelegate.h"
#import "GameScene.h"
@import AVFoundation;
@import QuartzCore;
@import AppKit;

@implementation SKScene (Unarchive)

+ (instancetype)unarchiveFromFile:(NSString *)file {
    /* Retrieve scene file path from the application bundle */
    NSString *nodePath = [[NSBundle mainBundle] pathForResource:file ofType:@"sks"];
    /* Unarchive the file to an SKScene object */
    NSData *data = [NSData dataWithContentsOfFile:nodePath
                                          options:NSDataReadingMappedIfSafe
                                            error:nil];
    NSKeyedUnarchiver *arch = [[NSKeyedUnarchiver alloc] initForReadingWithData:data];
    [arch setClass:self forClassName:@"SKScene"];
    SKScene *scene = [arch decodeObjectForKey:NSKeyedArchiveRootObjectKey];
    [arch finishDecoding];
        
    return scene;
}

@end

@implementation AppDelegate

@synthesize window = _window;

- (void)onSelect:(NSMenuItem *)sender {
   [self windowDidResize:nil];
   [_capture selectCamera:[sender title]];
}

- (IBAction)onMirrored:(NSMenuItem *)sender {
   [_capture setMirror:([sender state] == NSOffState)];
}

- (IBAction)onOutput:(NSMenuItem *)sender {
   [_capture enableOutput:([sender state] == NSOffState)];
}

- (BOOL)validateUserInterfaceItem:(id<NSValidatedUserInterfaceItem>)item {

   SEL theAction = [item action];
   
   NSString * selectedName = _capture.current;
   
   if (theAction == @selector(onSelect:)) {
      NSMenuItem *menu = (NSMenuItem *)item;
      
      if ([menu title] == selectedName) {
         [menu setState: NSOnState];
      } else {
         [menu setState: NSOffState];
      }
   } else if (theAction == @selector(onMirrored:)) {
      NSMenuItem *menu = (NSMenuItem *)item;
      [menu setState:([_capture isMirrored] ? NSOnState : NSOffState)];
   } else if (theAction == @selector(onOutput:)) {
      NSMenuItem *menu = (NSMenuItem *)item;
      [menu setState:([_capture isOutputEnabled] ? NSOnState : NSOffState)];
   }

   printf("fuck\n");
   return YES;
}

- (void)refreshCameraList:(NSNotification *)notification {

   [_cameras removeAllItems];
   NSArray *camList = [_capture getCameraList];
   for (NSString *name in camList) {
      [_cameras addItemWithTitle:name action:@selector(onSelect:) keyEquivalent:@""];
   }
}

- (void)applicationDidFinishLaunching:(NSNotification *)aNotification {
    GameScene *scene = [GameScene unarchiveFromFile:@"GameScene"];

    /* Set the scale mode to scale to fit the window */
    scene.scaleMode = SKSceneScaleModeAspectFit;

    [self.skView presentScene:scene];

    /* Sprite Kit applies additional optimizations to improve rendering performance */
    self.skView.ignoresSiblingOrder = YES;
    
    self.skView.showsFPS = YES;
    self.skView.showsNodeCount = YES;
   
   NSMenu *mainMenu = [NSApp mainMenu];
   NSMenuItem * item = [mainMenu itemWithTitle:@"Camera"];
   
   _cameras = [[NSMenu alloc] init];
   
   _cameras.title = @"Camera";
   _cameras.autoenablesItems = YES;
   item.submenu = _cameras;
   
   _imageProcess = [[ImageProcessor alloc] init];
   _capture = [[CameraCapture alloc] init];
   
   [_capture subscribe:_imageProcess];
   
   _imageProcess.drawLayer.zPosition = 1.;
   _imageProcess.drawLayer.opacity = 0.5;
   _imageProcess.drawLayer.frame = _skView.bounds;
   _imageProcess.drawLayer.bounds = _skView.bounds;
   [_skView.layer addSublayer:_imageProcess.drawLayer];
   [_skView.layer setNeedsDisplay];
   
   [self refreshCameraList:nil];

   [_capture setBounds:_skView.bounds];

   CALayer *layer = [_capture getPreviewLayer];
//   layer.frame = _skView.bounds;
   layer.opacity = 0.5;
   [_skView.layer addSublayer:layer];

   NSString* name = [[_cameras itemArray][0] title];
   [_capture selectCamera:name];

   NSNotificationCenter *center = [NSNotificationCenter defaultCenter];
   [center addObserver: self
              selector: @selector(refreshCameraList:)
                  name: @"CameraListUpdated"
                object: _capture];
                
   [center addObserver: self
              selector: @selector(windowDidResize:)
                  name: NSWindowDidResizeNotification
                object: nil];
   
   [_capture enableOutput:true];

}

- (void)windowDidResize:(NSNotification *)notification {
   [_capture setBounds:_skView.bounds];
//   [_capture getPreviewLayer].frame = _skView.bounds;
//   _previewLayer.frame = _skView.bounds;
   _imageProcess.drawLayer.frame = _capture.previewLayer.bounds;
}

- (BOOL)applicationShouldTerminateAfterLastWindowClosed:(NSApplication *)sender {
    return YES;
}

//- (void)drawLayer:(CALayer *)theLayer inContext:(CGContextRef)theContext {
//   CGMutablePathRef thePath = CGPathCreateMutable();
//   
//   CGPathMoveToPoint(thePath,NULL,15.0f,15.f);
//   CGPathAddCurveToPoint(thePath,
//                         NULL,
//                         15.f,250.0f,
//                         295.0f,250.0f,
//                         295.0f,15.0f);
//   
//   CGContextBeginPath(theContext);
//   CGContextAddPath(theContext, thePath);
//   
//   CGContextSetLineWidth(theContext, 5);
//   CGContextStrokePath(theContext);
//   
//   // Release the path
//   CFRelease(thePath);
//}

@end

