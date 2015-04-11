//
//  RouteViewController.h
//  Skånetrafiken 2.0
//
//  Created by Jacob Arvidsson on 2015-04-11.
//  Copyright (c) 2015 jacobarvidsson. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Route.h"

@interface RouteViewController : UIViewController<UITableViewDelegate, UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property Route *stations;
- (IBAction)back:(id)sender;
@end
