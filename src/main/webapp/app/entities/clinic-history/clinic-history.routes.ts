import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ClinicHistoryComponent } from './list/clinic-history.component';
import { ClinicHistoryDetailComponent } from './detail/clinic-history-detail.component';
import { ClinicHistoryUpdateComponent } from './update/clinic-history-update.component';
import ClinicHistoryResolve from './route/clinic-history-routing-resolve.service';

const clinicHistoryRoute: Routes = [
  {
    path: '',
    component: ClinicHistoryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClinicHistoryDetailComponent,
    resolve: {
      clinicHistory: ClinicHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClinicHistoryUpdateComponent,
    resolve: {
      clinicHistory: ClinicHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClinicHistoryUpdateComponent,
    resolve: {
      clinicHistory: ClinicHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default clinicHistoryRoute;
