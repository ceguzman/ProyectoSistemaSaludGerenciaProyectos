import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMedicalAuthorization } from '../medical-authorization.model';
import { MedicalAuthorizationService } from '../service/medical-authorization.service';

@Component({
  standalone: true,
  templateUrl: './medical-authorization-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MedicalAuthorizationDeleteDialogComponent {
  medicalAuthorization?: IMedicalAuthorization;

  protected medicalAuthorizationService = inject(MedicalAuthorizationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicalAuthorizationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
