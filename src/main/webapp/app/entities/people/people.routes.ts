import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PeopleComponent } from './list/people.component';
import { PeopleDetailComponent } from './detail/people-detail.component';
import { PeopleUpdateComponent } from './update/people-update.component';
import PeopleResolve from './route/people-routing-resolve.service';

const peopleRoute: Routes = [
  {
    path: '',
    component: PeopleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PeopleDetailComponent,
    resolve: {
      people: PeopleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PeopleUpdateComponent,
    resolve: {
      people: PeopleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PeopleUpdateComponent,
    resolve: {
      people: PeopleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default peopleRoute;
