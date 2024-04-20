import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MedicalAuthorizationComponent } from './list/medical-authorization.component';
import { MedicalAuthorizationDetailComponent } from './detail/medical-authorization-detail.component';
import { MedicalAuthorizationUpdateComponent } from './update/medical-authorization-update.component';
import MedicalAuthorizationResolve from './route/medical-authorization-routing-resolve.service';

const medicalAuthorizationRoute: Routes = [
  {
    path: '',
    component: MedicalAuthorizationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MedicalAuthorizationDetailComponent,
    resolve: {
      medicalAuthorization: MedicalAuthorizationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MedicalAuthorizationUpdateComponent,
    resolve: {
      medicalAuthorization: MedicalAuthorizationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MedicalAuthorizationUpdateComponent,
    resolve: {
      medicalAuthorization: MedicalAuthorizationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default medicalAuthorizationRoute;
