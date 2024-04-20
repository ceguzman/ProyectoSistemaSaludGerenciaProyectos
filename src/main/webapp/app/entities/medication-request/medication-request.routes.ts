import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MedicationRequestComponent } from './list/medication-request.component';
import { MedicationRequestDetailComponent } from './detail/medication-request-detail.component';
import { MedicationRequestUpdateComponent } from './update/medication-request-update.component';
import MedicationRequestResolve from './route/medication-request-routing-resolve.service';

const medicationRequestRoute: Routes = [
  {
    path: '',
    component: MedicationRequestComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MedicationRequestDetailComponent,
    resolve: {
      medicationRequest: MedicationRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MedicationRequestUpdateComponent,
    resolve: {
      medicationRequest: MedicationRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MedicationRequestUpdateComponent,
    resolve: {
      medicationRequest: MedicationRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default medicationRequestRoute;
