import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TypeDiseasesComponent } from './list/type-diseases.component';
import { TypeDiseasesDetailComponent } from './detail/type-diseases-detail.component';
import { TypeDiseasesUpdateComponent } from './update/type-diseases-update.component';
import TypeDiseasesResolve from './route/type-diseases-routing-resolve.service';

const typeDiseasesRoute: Routes = [
  {
    path: '',
    component: TypeDiseasesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeDiseasesDetailComponent,
    resolve: {
      typeDiseases: TypeDiseasesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeDiseasesUpdateComponent,
    resolve: {
      typeDiseases: TypeDiseasesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeDiseasesUpdateComponent,
    resolve: {
      typeDiseases: TypeDiseasesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeDiseasesRoute;
