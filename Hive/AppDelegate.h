//
//  AppDelegate.h
//  Hive
//

//  Copyright (c) 2015 Denis Kubasov. All rights reserved.
//

#import <Cocoa/Cocoa.h>
#import <SpriteKit/SpriteKit.h>

#include "CameraCapture.h"
#include "ImageProcessor.h"

@import AVFoundation;

@interface AppDelegate : NSObject <NSApplicationDelegate, NSUserInterfaceValidations>

- (void)onSelect:(NSMenuItem *)sender;
- (IBAction)onMirrored:(NSMenuItem *)sender;
- (IBAction)onOutput:(NSMenuItem *)sender;

- (BOOL)validateUserInterfaceItem:(id<NSValidatedUserInterfaceItem>)item;

@property (assign) IBOutlet NSWindow *window;
@property (assign) IBOutlet SKView *skView;

@property (strong) IBOutlet NSMenu *cameras;

@property (strong) CameraCapture *capture;
@property (strong) ImageProcessor *imageProcess;

@end
