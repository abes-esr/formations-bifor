/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.formation.writer;
import org.formation.model.Sessions;

/**
 *
 * @author jean-laurent
 */
public interface AgifWriter {
    public boolean EditListeStagiaires (Sessions s);
    public boolean EditEmargement (Sessions s);
    public boolean EditAttestation (Sessions s);
    public boolean EditChevalet (Sessions s);
}
