package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class HidableTableColumnModel implements TableColumnModel 
{

 
    protected ArrayList<TableColumn> m_allColumns    = null;
    protected TableColumnModel       m_delegateModel = null;    
    

 
     public HidableTableColumnModel( TableColumnModel delegateModel )
        throws NullPointerException
    {
        super();
        if (delegateModel == null)
            throw new NullPointerException("Cannot have null table column model");
        
        m_delegateModel = delegateModel;
        m_allColumns    = new ArrayList<TableColumn>( m_delegateModel.getColumnCount() );
        
        Enumeration<TableColumn> columns = m_delegateModel.getColumns();
        while (columns.hasMoreElements())
        {
            m_allColumns.add( columns.nextElement() );
        } 
    } 
    
     public void setColumnVisible( int viewIndex, boolean visible )
    {
        this.setColumnVisible(
                m_allColumns.get(viewIndex),
                visible);
    } 
    
    /**
     * @brief make the column visible or not
     * 
     * 
     *       
     * @param column    
     * @param visible   
     */
    public void setColumnVisible( TableColumn column, boolean visible )
    {
        if ( ! visible)
        {
            m_delegateModel.removeColumn(column);
        } 
        else
        {
            final int visibleColumns   = m_delegateModel.getColumnCount();
            final int invisibleColumns = m_allColumns.size();
                  int idxVisible       = 0;
            
            for (int idxInvisible = 0; idxInvisible < invisibleColumns; idxInvisible++)
            {
                final TableColumn visibleColumn = ( idxVisible < visibleColumns
                                                  ? m_delegateModel.getColumn(idxVisible)
                                                  : null);
                final TableColumn testColumn    = m_allColumns.get(idxInvisible);
                
                if (testColumn.equals(column))
                {
                    if ( ! column.equals(visibleColumn))
                    {
                        m_delegateModel.addColumn(column);
                        m_delegateModel.moveColumn(
                                m_delegateModel.getColumnCount() -1, 
                                idxVisible);
                    } 
                    return;
                } 
                
                if (testColumn.equals(visibleColumn))
                    idxVisible++;
            } 
        } 
    } 
    
    /**
     * @brief disable all column
     * 
     * this method makes all columns invisible
     */
    public void setAllColumnsInVisible() 
    {
        final int totalSize = m_allColumns.size();
        
        for (int idxColumn = 0; idxColumn < totalSize; idxColumn++)
        {
        	m_delegateModel.removeColumn(m_delegateModel.getColumn(m_delegateModel.getColumnIndexAtX(idxColumn)));
        } 
    } 
    
    /**
     * @brief shows all column
     * 
     * this method makes all columns visible
     */
    
    public void setAllColumnsVisible() 
    {
        final int totalSize = m_allColumns.size();
        
        for (int idxColumn = 0; idxColumn < totalSize; idxColumn++)
        {
            final TableColumn visibleColumn = ( idxColumn < m_delegateModel.getColumnCount()
                                              ? m_delegateModel.getColumn(idxColumn)
                                              : null);
            final TableColumn invisibleColumn = m_allColumns.get(idxColumn);
            
            if ( ! invisibleColumn.equals(visibleColumn))
            {
                m_delegateModel.addColumn(invisibleColumn);
                m_delegateModel.moveColumn(
                        m_delegateModel.getColumnCount() -1, 
                        idxColumn);
            } 
        } 
    } 
    
    /**
     * @brief getter for the column on index 
     * 
     * 
     * @param modelIndex    index of the column
     * @return              funded column or <code>null</code>
     */
    public TableColumn getColumnByModelIndex( int modelIndex )
    {
        for (int idxColumn = 0; idxColumn < m_allColumns.size(); idxColumn++)
        {
            final TableColumn column = m_allColumns.get(idxColumn);
            if (column.getModelIndex() == modelIndex)
                return column;
        } 
        
        return null;
    } 
    
    /**
     * @brief flag if column is visible or not
     * 
     * 
     * @param column    column
     * @return          <code>true</code> if the column is visible
     */
    public boolean isColumnVisible( TableColumn column ) 
    {
        for (int i = 0; i < m_delegateModel.getColumnCount(); i++)
        {
            if (m_delegateModel.getColumn(i).equals(column))
                return true;
        } 
        
        return false;
    } 
    
    /**
     * @brief Getter for the number of column
     * 
     * 
     * @param visibleOnly  if <code>true</code>, only visible columns will be counted
     * @return             number of the column   
     *
     * @see getColumnCount()                                       
     */
    public int getColumnCount( boolean visibleOnly )
    {
        if (visibleOnly)
            return m_delegateModel.getColumnCount();
        else
            return m_allColumns.size();
    } 
    
    /**
     * @brief enumeration of the column
     * 
     * 
     * @param visibleOnly   if <code>true</code>, only visible columns will be returned
     * @return              enumeration of the column
     *
     * @see getColumns()                                       
     */
    public Enumeration<TableColumn> getColumns( boolean visibleOnly )
    {
        if (visibleOnly)
        {
            return m_delegateModel.getColumns();
        } 
        else
        {
            return new Enumeration<TableColumn>()
            {
                private int m_index = 0;
                
                @Override
                public TableColumn nextElement()
                {
                    return m_allColumns.get(m_index++);
                } 
            
                @Override
                public boolean hasMoreElements()
                {
                    return (m_index < m_allColumns.size());
                } 
            };
        } 
    } 
    
    /**
     * @brief Getter for column index
     *        
     * 
     * @param identifier    
     * @param visibleOnly  
     * @return              index of the column
     * 
     * @throws IllegalArgumentException
     *  if the identifier is <code>null</code> or no column fund
     *  
     * @see getColumnIndex(Object) 
     */
    public int getColumnIndex( Object identifier, boolean visibleOnly )
        throws IllegalArgumentException
    {
        if (identifier == null)
            throw new IllegalArgumentException("Identifiert cannot be null");
        
        Enumeration<TableColumn> columns = this.getColumns(visibleOnly);
        int                      index   = 0;
        
        while (columns.hasMoreElements())
        {
            final Object id = columns.nextElement().getIdentifier();
            if (identifier.equals(id))
                return index;
            
            index++;
        } 
        
        throw new IllegalArgumentException("Identifier "+identifier+" not found");
    } 
 
    /**
     * @brief getter for colum index
     * 
     * 
     * @param columnIndex   index of the column
     * @param visibleOnly   needed if only visible column will be checked
     * @return              funded column 
     * 
     * @exception IndexOutOfBoundsException
     *  if the index is out of range
     *  
     * @see getColumn(int)
     */
    public TableColumn getColumn( int columnIndex, boolean visibleOnly ) 
    {
        if (visibleOnly)
            return m_delegateModel.getColumn(columnIndex);
        else
            return m_allColumns.get(columnIndex);
    } 
 
    /**
     * @brief methode for the actions
     * 
     * methode is used to work with all column visible or not
     * 
     * @code
     *  HidableTableColumnModel cModel  = ...;
     *  JPopupMenu              popup   = new JPopupMenu("Hide Menu");
     *  Action[]                actions = cModel.createColumnActions();
     *  for (Action act : actions)
     *  {
     *      popup.add(new JCheckBoxMenuItem(act));
     *  } //for
     *  myTable.setComponentPopupMenu(popup);
     * @endcode
     * 
     * @note the actions are  <i>not</i> serializable!
     * 
     * @return  array of actions per column
     */
    @SuppressWarnings("serial")
    public Action[] createColumnActions()
    {
        final Action[] actions = new Action[ m_allColumns.size() ];
        
        for (int i = 0; i < m_allColumns.size(); i++)
        {
            final TableColumn col = m_allColumns.get(i);
            final Action      act = new AbstractAction()
            {
                final private TableColumn internalColumn = col;
                
                {
                    this.putValue(
                            SELECTED_KEY, 
                            HidableTableColumnModel.this.isColumnVisible(col));
                    this.putValue(NAME, 
                            col.getHeaderValue());
                }
                
                @Override
                public void actionPerformed( ActionEvent event )
                {
                    HidableTableColumnModel.this.setColumnVisible(
                            internalColumn,
                            ((Boolean) this.getValue(SELECTED_KEY)).booleanValue());
                } 
            };
            
            actions[i] = act;
        } 
        
        return actions;
    } 
    
 

    /**
     * @brief adding a new column
     * 
     * 
     * @param column    new column
     */
    @Override
    public void addColumn( TableColumn column ) 
    {
        m_allColumns.add(column);
        m_delegateModel.addColumn(column);
    } 
 
    /**
     * @brief removes one column
     * 
     *
     * 
     * @param column    removing column
     */
    @Override
    public void removeColumn( TableColumn column ) 
    {
        m_allColumns.remove(column);
        m_delegateModel.removeColumn(column);
    } 
    
    /**
     * @brief move the column in the table
     * 
     * the method moves the column <code>oldIndex</code> to
     * <code>newIndex</code>. 
     * 
     * @param oldIndex  
     * @param newIndex  
     *                  
     * @throws IllegalArgumentException
     *  if the indices not valid                
     */
    @Override
    public void moveColumn( int oldIndex, int newIndex )
        throws IndexOutOfBoundsException
    {
        final int size = m_delegateModel.getColumnCount();
        if (   oldIndex < 0 || oldIndex >= size
            || newIndex < 0 || newIndex >= size)
        {
            throw new IndexOutOfBoundsException(
                    "At least one index is invalid: '"
                   +oldIndex
                   +"' or '"
                   +newIndex
                   +"' (valid range is [0, "
                   +size
                   +"])");
        } 
        
        final TableColumn oldColumn = m_delegateModel.getColumn(oldIndex);
        final TableColumn newColumn = m_delegateModel.getColumn(newIndex);
        final int         idxOld    = m_allColumns.indexOf(oldColumn);
        final int         idxNew    = m_allColumns.indexOf(newColumn);
        
        if (oldIndex != newIndex)
        {
            m_allColumns.remove(idxOld);
            m_allColumns.add(idxNew, oldColumn);
        } 
        
        m_delegateModel.moveColumn(oldIndex, newIndex);
    } 
 
    /**
     * @brief listener added
     * 
     * listener will be added
     * 
     * @param listener  new listener
     */
    @Override
    public void addColumnModelListener( TableColumnModelListener listener )
    {
        m_delegateModel.addColumnModelListener(listener);
    } 
 
    /**
     * @brief remove the listener
     * 
     * the listener will be removed of the model
     * 
     * @param listener  remove the listener
     */
    @Override
    public void removeColumnModelListener( TableColumnModelListener listener )
    {
        m_delegateModel.removeColumnModelListener(listener);
    } 
 
    /**
     * @brief getter column on index 
     * 

     * @param columnIndex   index of the column
     * @return              fund column
     * 
     * @exception IndexOutOfBoundsException
     *  if the index is out of range
     *  
     * @see getColumn(int, boolean)
     */
    @Override
    public TableColumn getColumn( int columnIndex )
    {
        return this.getColumn(columnIndex, true);
    } 
 
    /**
     * @brief getter for the amount of column
     * 
     * get the number of the all visible column
     * @return  number of visible column
     * 
     * @see getColumnCount(boolean)
     */
    @Override
    public int getColumnCount()
    {
        return this.getColumnCount(true);
    } 
 
    /**
     * @brief determine of the column
     * 
     * @param identifier   
     * @return              index of the fund column oder exception
     * 
     * @throws IllegalArgumentException
     *  if identifier is  <code>null</code>, or nothing fund
     * @see getColumnIndex(Object, boolean) 
     */
    @Override
    public int getColumnIndex( Object identifier ) throws IllegalArgumentException
    {
        return this.getColumnIndex(identifier, true);
    } 
 
    /**
     * @brief Getter for column on position x
     * 
     * 
     * @param position  poistion in pixel
     * @return          index of the column on position x
     */
    @Override
    public int getColumnIndexAtX( int position )
    {
        return m_delegateModel.getColumnIndexAtX(position);
    } 
 
    /**
     * @brief Getter for the column spacing
     * 
     * 
     * @return  spacing of the column in pixel
     * 
     * @see setColumnMargin(int)
     */
    @Override
    public int getColumnMargin()
    {
        return m_delegateModel.getColumnMargin();
    } 
 
    /**
     * @brief get ture if columnselection is allowed
     * 
     * 
     * @return  <code>true</code> if columnselection is allowed
     * 
     * @see setColumnSelectionAllowed(boolean)
     */
    @Override
    public boolean getColumnSelectionAllowed()
    {
        return m_delegateModel.getColumnSelectionAllowed();
    } 
 
    /**
     * @brief getter for all column
     * 
     * @return  enumeration of all visible column
     * 
     * @see getColumns(boolean)
     */
    @Override
    public Enumeration<TableColumn> getColumns()
    {
        return this.getColumns(true);
    } 
 
    /**
     * @brief get the number of selected column 
     * 
     * 
     * @return number of selected column 
     */
    @Override
    public int getSelectedColumnCount()
    {
        return m_delegateModel.getSelectedColumnCount();
    } 
 
    /**
     * @brief get the indices of selected column 
     * 
     * 
     * @return  array with the indices of the selected column 
     */
    @Override
    public int[] getSelectedColumns()
    {
        return m_delegateModel.getSelectedColumns();
    } 
 
    /**
     * @brief get the selection model
     * 
     * 
     * @return  selectionmodel
     */
    @Override
    public ListSelectionModel getSelectionModel()
    {
        return m_delegateModel.getSelectionModel();
    } 
 
    /**
     * @brief get the whole width of the column
     * 
     * 
     * @return  width of all columns in pixel
     */
    @Override
    public int getTotalColumnWidth()
    {
        return m_delegateModel.getTotalColumnWidth();
    } 
 
    /**
     * @brief setter for the column spacing
     * 
     * @param newMargin new Spacing
     * 
     * @see getColumnMargin()
     */
    @Override
    public void setColumnMargin( int newMargin )
    {
        m_delegateModel.setColumnMargin(newMargin);
    } 
 
    /**
     * @brief allow/deny selectionmodel
     * 
     * 
     * @param flag is selection is allowed or not
     * 
     * @see getColumnSelectionAllowed()
     */
    @Override
    public void setColumnSelectionAllowed( boolean flag )
    {
        m_delegateModel.setColumnSelectionAllowed(flag);
    } 
 
    /**
     * @brief setter for the selectionmodel
     * 
     * set a new model for the selectionmodel
     * 
     * @param a new model
     * 
     * @see getSelectionModel()
     */
    @Override
    public void setSelectionModel( ListSelectionModel model )
    {
        m_delegateModel.setSelectionModel(model);
    } 
    
} 