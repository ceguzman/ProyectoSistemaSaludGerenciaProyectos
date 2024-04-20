import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MedicalAppointmentsComponent } from './list/medical-appointments.component';
import { MedicalAppointmentsDetailComponent } from './detail/medical-appointments-detail.component';
import { MedicalAppointmentsUpdateComponent } from './update/medical-appointments-update.component';
import MedicalAppointmentsResolve from './route/medical-appointments-routing-resolve.service';

const medicalAppointmentsRoute: Routes = [
  {
    path: '',
    component: MedicalAppointmentsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MedicalAppointmentsDetailComponent,
    resolve: {
      medicalAppointments: MedicalAppointmentsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MedicalAppointmentsUpdateComponent,
    resolve: {
      medicalAppointments: MedicalAppointmentsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MedicalAppointmentsUpdateComponent,
    resolve: {
      medicalAppointments: MedicalAppointmentsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default medicalAppointmentsRoute;
